import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import java.sql.*;




public class Services
{
    private String[] S = new String[10];
    public Services()
    {
        S[0] = "cuantos";
        S[1] = "M";
        S[2] = "m";
        S[3] = "CRYPT";
        S[4] = "DECRYPT";
        S[5] = "I";
        S[6] = "B";
        S[7] = "TEMP";
        S[8] = "POST";
        S[9] = "SQL";   
    }


    public String cadena(String x)
    {
       String[] parts = x.split("#");
        return parts[2];
    }
    public String comando(String x)
    {
       int V=10;
       String[] parts = x.split("#");
       for(int i=0;i<=9;i++){
           if(parts[1].equals(S[i])){
            V=i;
            break;
           }
       } 
         switch (V) {
           case 0:
           String clientes= String.valueOf(ServerMultiClient.NoClients);
                return clientes;
           case 1:
                return M(parts[2]);
           case 2:
                return m(parts[2]);
           case 3:
                return CRYPT(parts[2]);
           case 4:
                return DECRYPT(parts[2]);
           case 5:
           StringBuilder invertido=new StringBuilder(cadena(x));
                return invertido.reverse().toString();
           case 6:
                int numEntero = Integer.parseInt(cadena(x).trim());
                return decToBin(numEntero);
           case 7:
                return getWebSite();
           case 8:
                return Publicar();
           default:
               return "No se encontro el Servicio";
       }
       
    }
    public static String decToBin(int numeroDecimal){
       return Integer.toBinaryString(numeroDecimal);
       }
 
    public static String CRYPT(String mensaje) {
       byte[] bytesDelMensaje = mensaje.getBytes();
 
       MessageDigest resumenDelMensaje = null;
       try {
           resumenDelMensaje = MessageDigest.getInstance("MD5");
           byte[] bytesDelResumen = resumenDelMensaje.digest(bytesDelMensaje);
 
           BigInteger resumenNumero = new BigInteger(1, bytesDelResumen);
           String resumen = resumenNumero.toString(16);
           return resumen;
          } catch (NoSuchAlgorithmException e) {}
         return "No se pudo encriptar";
    }
    public static String DECRYPT(String password) {
        if(password.equals("c6f00988430dbc8e83a7bc7ab5256346")){
          return "HOLA";    
        } else if(password.equals("ef59b594015c637163e4987c5d6e6828")){
           return "ADIOS";
        }
 
       return "No se pudo desencriptar";
    }
    public String m(String C){
        return C.toLowerCase();

    }
    public String M(String C){
        return C.toUpperCase();

    }
    public String getWebSite() {
        String Clima="";
        String Complemento="";
        try {
 
            URL url = new URL("https://www.accuweather.com/es/mx/mexico-city/242560/weather-forecast/242560");
            URLConnection urlc = url.openConnection();
 
            BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream());
 
            StringBuilder builder = new StringBuilder();
            int byteRead;
            while ((byteRead = buffer.read()) != -1)
                builder.append((char) byteRead);
 
            buffer.close();
            int i=builder.toString().indexOf("local-temp");
            if(Integer.parseInt(builder.substring(i+12, i+14))<=16){
                Complemento=" Hace Frio";
            }
            if(Integer.parseInt(builder.substring(i+12, i+14))>=17 && Integer.parseInt(builder.substring(i+12, i+14))<=26){
                Complemento=" Buen Clima";
            }
            if(Integer.parseInt(builder.substring(i+12, i+14))>=27){
                Complemento=" Hace Calor";
            }
            
            Clima ="El Clima en la CDMX es de: "+builder.substring(i+12, i+14)+"ºC"+Complemento;
            //System.out.println(i);
            //System.out.println(builder.substring(i+12, i+14));
            //System.out.println("The size of the web page is " + builder.length() + " bytes.");
 
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Clima;
    }

    public String Publicar(){
        String Token ="EAAgNUDMk3kUBANUZBz03bZBxyLwgxJpEkoz3YhvpLTCnOjff5NFw3WAGZAtiEzpZAom2ABfzzWaT2eeMhRcWbBtFoFv7aXJ4GcsZBJHncyajcvStnLMEkeMyTRmcjkfjg7CVZCNe9LsYQeZCZAgqymrF05LIZCOu0tzwaeNDAMuLcgw5z00dZCYixa7fK0nSCe2QS0ZAjL04E23bQZDZD";
        FacebookClient facebook = new DefaultFacebookClient(Token);
        String webSite = getWebSite();
        //Publicar Foto

        FacebookType response= facebook.publish("/me/feed", FacebookType.class, Parameter.with("message", webSite));
       //System.out.println("fb.com/"+response.getId());
       String respuesta ="fb.com/"+response.getId();

       return respuesta;
    }

    public static String peticion(String USERP) {
        String[] parts = USERP.split("/");
        String user= parts[1];
        String password= parts[2];
		String name="";
        String pass="";
        
        // Instancias la clase que hemos creado anteriormente
        ConexionMySQL SQL = new ConexionMySQL();
        // Llamas al método que tiene la clase y te devuelve una conexión
        Connection con = SQL.conectarMySQL();

        try  {
            PreparedStatement stmt = con.prepareStatement("SELECT name,pass FROM users WHERE name='"+user+"'");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                name =rs.getString(1);
                pass =rs.getString(2);
        }
           
          } catch (SQLException sqle) { 
          
             

                      }
                      if(name.equals(user) && pass.equals(password)){
                        return "YES";
                      }else{
                          return "NO";
                      }
	}
}