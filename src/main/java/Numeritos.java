import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Numeritos {


    public void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/Users/Sergio Malagon/Documents/numeritos.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public Manga hMetadata(int numeritos) {

        try {
            Manga salida = new Manga();
            URL url = new URL("https://nhentai.net/api/gallery/" + String.valueOf(numeritos));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();
                JSONObject object = new JSONObject(inline);

                //Obtengo el id del manga
                salida.setId(object.getInt("id"));

                //Obtengo el autor del manga
                JSONArray arr = object.getJSONArray("tags");
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getJSONObject(i).getString("type").equals("artist")) {
                        salida.setAutor(arr.getJSONObject(i).getString("name"));
                    }
                }

                //Obtengo el numero de paginas del manga
                salida.setPaginas(object.getInt("num_pages"));

                //Obtengo los tags
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getJSONObject(i).getString("type").equals("tag")) {
                        sb.append(arr.getJSONObject(i).getString("name"));
                        sb.append("|");
                    }
                }
                salida.setTags(sb.toString());


                //Obtengo la parodia
                for (int i = 0; i < arr.length(); i++) {

                    if (arr.getJSONObject(i).getString("type").equals("parody")) {
                        salida.setParodia(arr.getJSONObject(i).getString("name"));
                        break;
                    }else {
                        salida.setParodia("original");
                    }
                }

                //Obtengo los personajes
                sb = new StringBuilder();
                if (!salida.getParodia().equals("original")) {
                    for (int i = 0; i < arr.length(); i++) {
                        if (arr.getJSONObject(i).getString("type").equals("character")) {
                            sb.append(arr.getJSONObject(i).getString("name"));
                            sb.append("|");
                        }
                    }
                    salida.setPersonaje(sb.toString());
                } else if (salida.getParodia().equals("original")) {
                    salida.setPersonaje("");
                }

                //Obtengo el titulo
                salida.setTitulo(object.getJSONObject("title").getString("pretty"));

                //Obtengo la experiancia


                //Obtengo la media_id
                salida.setMedia(Integer.parseInt(object.getString("media_id")));

                System.out.println(salida.toString());
            }
            return salida;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    public void insert(Manga manga) {

    }

}