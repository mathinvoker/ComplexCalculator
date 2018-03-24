package com.complexcalc.matheus.complexcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    char [] symbols = {'*', '+', '/', '-'}; // nao pode deixar o '-' no inicio senao da errado...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public char symbol(String expression, char [] symbols){
        for(int i = 0; i < symbols.length; i++){
            if(expression.indexOf(symbols[i]) != -1)
                return symbols[i];
        }
        return ' ';
    }

    public int numberOfCharacters(String expression, char tag){
        int n = 0;
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) == tag)
                n = n + 1;
        }
        return n;
    }

    public void pressedNumberAction(View v){

        TextView screen = (TextView) findViewById(R.id.screenText);
        String expression = screen.getText().toString();
        String tag = v.getTag().toString();

        expression = expression + tag;
        screen.setText(expression);
    }

    public void clearButtonAction(View v){

        TextView screen = (TextView) findViewById(R.id.screenText);
        screen.setText("");
    }

    public void backspaceButtonAction(View v){

        TextView screen = (TextView) findViewById(R.id.screenText);
        String expression = screen.getText().toString();

        try{
            int length = expression.length();
            expression = expression.substring(0, length-1);
            screen.setText(expression);
        }catch(StringIndexOutOfBoundsException s){
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Não há nada para apagar!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void pressedOperationAction(View v){

        TextView screen = (TextView) findViewById(R.id.screenText);
        String expression = screen.getText().toString();
        String tag = v.getTag().toString();

        if(tag.equals("=")){
            // resolve a equacao
            String operation = symbol(expression, symbols) + "";
            if(operation != " "){
                try{
                    String [] value = expression.split(Pattern.quote(operation));
                    double result;
                    if(value.length == 2){
                        System.out.println("Valor 0: " + Double.parseDouble(value[0]));
                        System.out.println("Valor 1: " + Double.parseDouble(value[1]));
                        System.out.println("Operacao: " + operation);

                        if(operation.equals("+")) {
                            result = Double.parseDouble(value[0]) + Double.parseDouble(value[1]);
                            screen.setText(String.format("%.2f", result));
                        }else if(operation.equals("-")) {
                            result = Double.parseDouble(value[0]) - Double.parseDouble(value[1]);
                            screen.setText(String.format("%.2f", result));
                        }else if(operation.equals("*")) {
                            result = Double.parseDouble(value[0]) * Double.parseDouble(value[1]);
                            screen.setText(String.format("%.2f", result));
                        } else {
                            result = Double.parseDouble(value[0]) / Double.parseDouble(value[1]);
                            if(Double.parseDouble(value[1]) == 0.0){
                                Context context = getApplicationContext();
                                Toast toast = Toast.makeText(context, "O segundo operando não pode ser igual a zero!", Toast.LENGTH_LONG);
                                toast.show();
                            } else{
                                screen.setText(String.format("%.2f", result));
                            }
                        }
                    }
                }catch(NumberFormatException n){
                    // caso o segundo parametro nao tenha nenhum valor...
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "Ocorreu algum problema na formatação da expressão!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        } else {
            int length = expression.length();

            // verifica se ja existe alguma operacao
            int indexes = expression.indexOf('+') + expression.indexOf('/') + expression.indexOf('*') + expression.indexOf('-');

            if(indexes == -4 || (expression.indexOf('-') == 0)){
                if(length > 0) {
                    if (Character.isDigit(expression.charAt(length - 1))) {
                        expression = expression + tag;
                        screen.setText(expression);
                    } else if (expression.charAt(length - 1) == '.') {
                        expression = expression + "0" + tag;
                        screen.setText(expression);
                    } else {
                        screen.setText(expression);
                    }
                }else{
                    screen.setText(expression);
                }
            } else{
                // nao adiciona mais nenhuma operacao
            }
        }

    }

    public void pressedDotAction(View v){
        TextView screen = (TextView) findViewById(R.id.screenText);
        String expression = screen.getText().toString();

        int length = expression.length();
        int num = 2; // numero maximo de pontos em uma expressao
        if(symbol(expression, symbols) == ' '){
            if(expression.indexOf('.') == -1){
                if(length == 0){
                    expression = expression + "0.";
                    screen.setText(expression);
                }else{
                    expression = expression + ".";
                    screen.setText(expression);
                }
            } else {
                screen.setText(expression);
            }
        }else{
            if(numberOfCharacters(expression.substring(expression.indexOf(symbol(expression, symbols))), '.') == 0){
                if (Character.isDigit(expression.charAt(length - 1))) {
                    expression = expression + ".";
                    screen.setText(expression);
                }else {
                    expression = expression + "0.";
                    screen.setText(expression);
                }
            }
        }
    }
}
