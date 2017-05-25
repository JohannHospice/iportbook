package com.iportbook.app.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    public enum Type {
        REGIS("REGIS"), WELCO("WELCO"), GOBYE("GOBYE"), CONNE("CONNE"), HELLO("HELLO"), FRIE("FRIE"), MENUM("MENUM"),
        MESS("MESS"), FLOO("FLOO"), LIST("LIST"), RLIST("RLIST"), LINUM("LINUM"), CONSU("CONSU"), SSEM("SSEM"),
        MUNEM("MUNEM"), OOLF("OOLF"), EIRF("EIRF"), OKIRF("OKIRF"), NOKRF("NOKRF"), ACKRF("ACKRF"), FRIEN("FRIEN"),
        NOFRI("NOFRI"), LBUP("LBUP"), NOCON("NOCON"), PUBL("PUBL");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Type hasType(String str) {
            for (Type type : Type.values()) {
                if (type.value.equalsIgnoreCase(str))
                    return type;
            }
            return null;
        }
    }

    public enum Operator {
        ASK("?"), CLEFT("<"), CRIGHT(">"), NONE("");
        private final String value;

        Operator(String value) {
            this.value = value;
        }

        public static Operator hasType(String str) {
            for (Operator type : Operator.values()) {
                if (type.value.equalsIgnoreCase(str))
                    return type;
            }
            return null;
        }
    }

    private Type type;
    private Operator operator;
    private ArrayList<String> arguments = new ArrayList<>();

    private final static String regex = "^([A-Z]+)([\\?><]?)((\\s[a-zA-Z0-9]+)*)?\\+\\+\\+$";
    private final static Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

    public Message(Type type, Operator operator) {
        this.type = type;
        this.operator = operator;
    }

    public Message(Type type, Operator operator, String[] args) {
        this(type, operator);
        this.arguments.addAll(Arrays.asList(args));
    }

    public static Message parse(String text) throws Exception {
        Message message = null;
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            Type type = Type.hasType(matcher.group(0));
            Operator operator = Operator.hasType(matcher.group(1));
            String[] args = matcher.group(2).split(" ");

            if (type == null || operator == null)
                throw new Exception("message not valid");

            message = new Message(type, operator, args);
        }

        return message;
    }

    public String compose() {
        StringBuilder concat = new StringBuilder();
        for (String str : arguments)
            concat.append(" ").append(str);
        return type.value + operator.value + concat.toString() + "+++";
    }

    public void addArgument(String arg) {
        arguments.add(arg);
    }

    public String getArgument(int i) {
        return arguments.get(i);
    }

    public int getArgumentSize() {
        return arguments.size();
    }

    public Operator getOperator() {
        return operator;
    }

    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}