package code;
public class Token {
    String type;  // Token 类型，例如 "KEYWORD", "IDN", "INT"
    String value; // Token 值，例如 "if", "x", "123" 还有代表的数字
    String number;   // Token 对应的数字 //如果需要
    
    public Token(String type, String value) {
        this.type = type;
        this.value = value;
        //根据类型判断是否在number中填入数字
        /*
        关键字（KW，不区分⼤⼩写）包括： (1) int (2) float (3) char (4) void (5) return (6) const (7) main
        运算符（OP，不区分⼤⼩写）包括： (8) ! (9) + (10) - (11) * (12) / (13) % (14) = (15) > (16) < (17) == (18) <= (19) >= (20) != (21) && (22) ||
        界符（SE）包括：(23)（ (24) ） (25) { (26) } (27)； (28) ,
         */
        if(type.equals("KW")) {
            switch (value) {
                case "int" -> this.number = "1";
                case "float" -> this.number = "2";
                case "char" -> this.number = "3";
                case "void" -> this.number = "4";
                case "return" -> this.number = "5";
                case "const" -> this.number = "6";
                case "main" -> this.number = "7";
                case "while" -> this.number = "29";
                case "for" -> this.number = "30";
                case "break" -> this.number = "31";
                case "if" -> this.number = "32";
                case "else" -> this.number = "33";
                case "struct" -> this.number = "34";
                case "union" -> this.number = "35";
                case "switch" -> this.number = "36";
                case "case" -> this.number = "37";
                case "default" -> this.number = "38";
            }
        } else if (type.equals("OP")) {
            switch (value) {
                case "!" -> this.number = "8";
                case "+" -> this.number = "9";
                case "-" -> this.number = "10";
                case "*" -> this.number = "11";
                case "/" -> this.number = "12";
                case "%" -> this.number = "13";
                case "=" -> this.number = "14";
                case ">" -> this.number = "15";
                case "<" -> this.number = "16";
                case "==" -> this.number = "17";
                case "<=" -> this.number = "18";
                case ">=" -> this.number = "19";
                case "!=" -> this.number = "20";
                case "&&" -> this.number = "21";
                case "||" -> this.number = "22";
            }
        } else if (type.equals("SE")) {
            switch (value) {
                case "(" -> this.number = "23";
                case ")" -> this.number = "24";
                case "{" -> this.number = "25";
                case "}" -> this.number = "26";
                case ";" -> this.number = "27";
                case "," -> this.number = "28";
                case ":" -> this.number = "39";
            }
        } else {
            this.number = value;
        }
    }

    @Override
    public String toString() {
        return value + "\t<" + type + "," + number + ">";
    }


}

