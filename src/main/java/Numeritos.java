import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Numeritos {

  private int lineasNuevas = 0;
  private Connection conn = null;
  private Map<String, Double> map = new HashMap<String, Double>();

  public void connect() {
    try {
      // db parameters
      String url = "jdbc:sqlite:C:/Users/Sergio Malagon/Documents/numeritos.db";
      // create a connection to the database
      conn = DriverManager.getConnection(url);

      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void close() {
    try {
      this.conn.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public boolean obtainLineas(String routa) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(routa));
      String line = reader.readLine();
      this.connect();
      while (line != null) {
        insert(hMetadata(line.trim()));
        line = reader.readLine();
      }
      reader.close();
      this.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public Manga hMetadata(String numeritos) {
    try {
      Manga salida = new Manga();
      URL url = new URL("https://nhentai.net/api/gallery/" + numeritos);
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
          } else {
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
        String tags = salida.getTags();
        String[] parts = tags.split("|");
        int i = 0;
        double xpTotal, xpPagina;
        double xp;
        xpPagina = 10;
        xpTotal = xpPagina * salida.getPaginas();
        /*
        while (i < parts.length) {
          if (this.xpValues.containsKey(parts[i])) {
            xpTotal += 100 * this.xpValues.get(parts[i]);
          } else {
            xpTotal += 100 * 1;
          }
        }
*/
        while (i < parts.length) {
          xp = this.xpValues.get(parts[i]);
          if (xp == null) {
            xp = 1;
          }
          xpTotal += 100 * xp;
        }
        salida.setXP(xpTotal);

        //Obtengo el media_id
        salida.setMedia(Integer.parseInt(object.getString("media_id")));

        System.out.println(salida.toString());
      }
      return salida;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void cargarValoresXp() {
    String linea;
    File archivo = null;
    FileReader fr = null;
    BufferedReader br = null;
    String[] parts;
    try {
      archivo = new File("C:\\Experiencia.txt");
      fr = new FileReader(archivo);
      br = new BufferedReader(fr);

      linea = b.readLine();
      while (linea != null) {
        parts = linea.split(",");
        map.put(parts[0], Double.parseDouble(parts[1]));
        linea = b.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != fr) {
          fr.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
  }

  public void insert(Manga manga) {
    String sql =
      "INSERT INTO Numeritos(id,autor,paginas,tags,parodia,personaje,titulo,exp,media) VALUES(?,?,?,?,?,?,?,?,?)";
    try {
      PreparedStatement pstmt = this.conn.prepareStatement(sql);
      pstmt.setInt(1, manga.getId());
      pstmt.setString(2, manga.getAutor());
      pstmt.setInt(3, manga.getPaginas());
      pstmt.setString(4, manga.getTags());
      pstmt.setString(5, manga.getParodia());
      pstmt.setString(6, manga.getPersonaje());
      pstmt.setString(7, manga.getTitulo());
      pstmt.setFloat(8, manga.getExp());
      pstmt.setInt(9, manga.getMedia());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    lineasNuevas++;
  }

  public int getLineasNuevas() {
    return lineasNuevas;
  }
}
