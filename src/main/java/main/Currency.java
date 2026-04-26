package main;

public class Currency {
    private String code;
    private String name;
    private double index;

    public Currency createCurr(String code, String name, double index){
        if (code.length() == 3 && index > 0){
            this.code = code;
            this.name = name;
            this.index = 1 / index;
            return this;
        }
        else{
            return null;
        }
    }

    @Override
    public String toString(){
        return code + " " + name + "\t" + index;
    }

}
