package gvo.util.item;
public class Method {
         
          private String Action;
          private String value;
         
          public Method(){
          }
         
          public Method(String value){
               this.value = value;
          }
         
          public Method(String Action, String value){
               this.Action = Action;
               this.value = value;
          }
    
          public String getAction() {
               return Action;
          }
    
          public void setAction(String action) {
               Action = action;
          }
    
          public String getValue() {
               return value;
          }
    
          public void setValue(String value) {
               this.value = value;
          }
    
          @Override
          public String toString() {
               return "Method [Action=" + Action + ", value=" + value + "]";
          }
    
     }