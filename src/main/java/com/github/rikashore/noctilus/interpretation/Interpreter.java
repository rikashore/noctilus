package com.github.rikashore.noctilus.interpretation;

import com.github.rikashore.noctilus.values.*;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Interpreter {
    private final KnFunction function;
    private final HashMap<String, KnValue> variables;

    public Interpreter(KnFunction function) {
        this.function = function;
        this.variables = new HashMap<>();
    }

    public void run() {
        this.evaluateFunction(this.function);
    }

    private KnValue evaluate(KnValue value) {
        return switch (value) {
            case KnInt knInt -> knInt;
            case KnString knString -> knString;
            case KnVariable knVariable -> this.variables.getOrDefault(knVariable.name, new KnNil());
            case KnFunction knFunction -> this.evaluateFunction(knFunction);
            case KnList knl -> knl;
            default -> throw new RuntimeException("Did you forget a case");
        };
    }

    private KnValue evaluateFunction(KnFunction fn) {
        return switch (fn.name) {
            // NULLARY
            case "P" -> {
                var inp = new Scanner(System.in);
                var s = inp.nextLine();
                inp.close();
                var val = s.stripTrailing();

                yield new KnString(val);
            }
            case "R" -> {
                var rng = new Random();
                int n = rng.nextInt(0, Integer.MAX_VALUE);

                yield new KnInt(n);
            }

            // UNARY
            case "B" -> new KnBlock(fn.args[0]);

            case "C" -> {
                var arg = evaluate(fn.args[0]);

                if (arg instanceof KnNil)
                    throw new UnexpectedTypeException("No block with name found");


                if (!(arg instanceof KnBlock block))
                    throw new RuntimeException("Expected block, got" + arg.getDebugRepresentation());

                yield evaluate(block.value);
            }

            case "Q" -> {
                var evaluated = evaluate(fn.args[0]);

                if (!(evaluated instanceof KnInt kni))
                    throw new UnexpectedTypeException("Expected integer but got" + evaluated.getDebugRepresentation());

                var exitCode = kni.value >= 0 && kni.value <= 127 ? kni.value : 1;

                System.exit(exitCode);
                yield new KnNil(); // to satisfy Java for some reason
            }

            case "O" -> {
                var evaluated = evaluate(fn.args[0]);
                var coerced = coerceToString(evaluated);

                if (coerced.value.endsWith("\\")) {
                    System.out.print(coerced.value.substring(0, coerced.value.length() - 1));
                } else {
                    System.out.println(coerced.value);
                }

                yield new KnNil();
            }


            case "D" -> {
                var evaluated = evaluate(fn.args[0]);
                var output = evaluated.getDebugRepresentation();
                System.out.println(output);
                yield evaluated;
            }

            case "!" -> {
                var coercedVal = coerceToBool(evaluate(fn.args[0]));
                yield new KnBool(!coercedVal.value);
            }

            case "~" -> {
                var coercedVal = coerceToInt(evaluate(fn.args[0]));
                yield new KnInt(-coercedVal.value);
            }

            case "A" -> switch (evaluate(fn.args[0])) {
                case KnInt kni -> new KnString(Character.toString(kni.value));
                case KnString kns -> new KnInt(Character.codePointAt(kns.value, 0));
                default -> throw new UnexpectedTypeException("Expected Integer or String, got " + fn.args[0].getDebugRepresentation());
            };

            // BINARY

            case "+" -> switch (evaluate(fn.args[0])) {
                case KnInt kni -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToInt(evaluated);

                    yield new KnInt(kni.value + coerced.value);
                }
                case KnString kns -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToString(evaluated);

                    yield new KnString(kns.value + coerced.value);
                }
                default -> throw new UnexpectedTypeException("Expected Integer, List or String, got" + fn.args[0].getDebugRepresentation());
            };

            case "-" -> {
                var evaluated = evaluate(fn.args[0]);

                if (!(evaluated instanceof KnInt kni))
                    throw new UnexpectedTypeException("Expected Integer, got" + fn.args[0].getDebugRepresentation());

                var second = evaluate(fn.args[1]);
                var arg = coerceToInt(second);

                yield new KnInt(kni.value - arg.value);
            }

            case "*" -> switch (evaluate(fn.args[0])) {
                case KnInt kni -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToInt(evaluated);

                    yield new KnInt(kni.value * coerced.value);
                }
                case KnString kns -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToInt(evaluated);

                    if (coerced.value < 0)
                        throw new RuntimeException("Cannot multiply string by negative length");

                    yield new KnString(kns.value.repeat(coerced.value));
                }
                default -> throw new UnexpectedTypeException("Expected Integer, List or String, got" + fn.args[0].getDebugRepresentation());
            };

            case "/" -> {
                var evaluated = evaluate(fn.args[0]);

                if (!(evaluated instanceof KnInt kni))
                    throw new UnexpectedTypeException("Expected Integer, got" + fn.args[0].getDebugRepresentation());

                var second = evaluate(fn.args[1]);
                var arg = coerceToInt(second);

                if (arg.value == 0)
                    throw new RuntimeException("Division by zero is not permitted");

                yield new KnInt(kni.value / arg.value);
            }

            case "%" -> {
                var evaluated = evaluate(fn.args[0]);

                if (!(evaluated instanceof KnInt kni))
                    throw new UnexpectedTypeException("Expected Integer, got" + fn.args[0].getDebugRepresentation());

                if (kni.value < 0)
                    throw new RuntimeException("Expected positive integer or 0");

                var second = evaluate(fn.args[1]);
                var arg = coerceToInt(second);

                if (arg.value <= 0)
                    throw new RuntimeException("Expected positive argument");

                yield new KnInt(kni.value % arg.value);
            }

            case "<" -> switch (evaluate(fn.args[0])) {
                case KnInt kni -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToInt(evaluated);

                    yield new KnBool(kni.value < coerced.value);
                }
                case KnString kns -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToString(evaluated);

                    yield new KnBool(kns.value.compareTo(coerced.value) < 0);
                }
                case KnBool knb -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToBool(evaluated);

                    yield new KnBool(!knb.value && coerced.value);
                }
                default -> throw new UnexpectedTypeException("Expected Integer, List or String, got" + fn.args[0].getDebugRepresentation());
            };

            case ">" -> switch (evaluate(fn.args[0])) {
                case KnInt kni -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToInt(evaluated);

                    yield new KnBool(kni.value > coerced.value);
                }
                case KnString kns -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToString(evaluated);

                    yield new KnBool(kns.value.compareTo(coerced.value) > 0);
                }
                case KnBool knb -> {
                    var evaluated = evaluate(fn.args[1]);
                    var coerced = coerceToBool(evaluated);

                    yield new KnBool(knb.value && !coerced.value);
                }
                default -> throw new UnexpectedTypeException("Expected Integer, List or String, got" + fn.args[0].getDebugRepresentation());
            };

            case "&" -> {
                var first = evaluate(fn.args[0]);
                var coerced = coerceToBool(first);

                if (!coerced.value)
                    yield first;
                else
                    yield evaluate(fn.args[1]);
            }

            case "|" -> {
                var first = evaluate(fn.args[0]);
                var coerced = coerceToBool(first);

                if (coerced.value)
                    yield first;
                else
                    yield evaluate(fn.args[1]);
            }

            case ";" -> {
                evaluate(fn.args[0]);
                yield evaluate(fn.args[1]);
            }

            case "=" -> {
                if (!(fn.args[0] instanceof KnVariable variable))
                    throw new UnexpectedTypeException("Expected Variable Name, got" + fn.args[0].getDebugRepresentation());

                var evaluated = evaluate(fn.args[1]);
                this.variables.put(variable.name, evaluated);

                yield evaluated;
            }

            case "W" -> {
                while (true) {
                    var condition = coerceToBool(evaluate(fn.args[0]));

                    if (!condition.value)
                        break;

                    evaluate(fn.args[1]);
                }

                yield new KnNil();
            }

            // TERNARY

            case "I" -> {
                var condition = coerceToBool(evaluate(fn.args[0]));

                if (condition.value)
                    yield evaluate(fn.args[1]);

                yield evaluate(fn.args[2]);
            }


            default -> throw new IllegalStateException("How are we here?");
        };
    }

    private KnInt coerceToInt(KnValue value) {
        return switch (value) {
            case KnNil _ -> new KnInt(0);
            case KnInt kni -> kni;
            case KnString kns -> {
                var i = 0;
                while (i < kns.value.length() && Character.isDigit(kns.value.charAt(i))) {
                    i++;
                }

                var sub = kns.value.substring(0, i);

                yield new KnInt(sub.isEmpty() ? 0 : Integer.parseInt(sub));
            }
            case KnBool knb -> new KnInt(knb.value ? 1 : 0);
            case KnList knl -> new KnInt(knl.backing.size());
            default -> throw new IllegalStateException("What");
        };
    }

    private KnBool coerceToBool(KnValue value) {
        return switch (value) {
            case KnNil _ -> new KnBool(false);
            case KnInt kni -> new KnBool(kni.value != 0);
            case KnString kns -> new KnBool(!kns.value.isEmpty());
            case KnBool knb -> knb;
            case KnList knl -> new KnBool(!knl.backing.isEmpty());
            default -> throw new IllegalStateException("What" + value);
        };
    }

    private KnString coerceToString(KnValue value) {
        return switch (value) {
            case KnNil _ -> new KnString("");
            case KnInt kni -> new KnString(Integer.toString(kni.value));
            case KnString kns -> kns;
            case KnBool knb -> new KnString(knb.value ? "true" : "false");
            case KnList knl -> {
                if (knl.backing.isEmpty())
                    yield new KnString("");

                var representation = knl.backing.stream()
                        .map(this::coerceToString)
                        .map(kv -> kv.value)
                        .collect(Collectors.joining("\n"));

                yield new KnString(representation);
            }
            default -> throw new IllegalStateException("What");
        };
    }
}
