public class Manga {
    private int id;
    private String autor;
    private int paginas;
    private String tags;
    private String parodia;
    private String personaje;
    private String titulo;
    private float exp;
    private int media;

    public Manga() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getParodia() {
        return parodia;
    }

    public void setParodia(String parodia) {
        this.parodia = parodia;
    }

    public String getPersonaje() {
        return personaje;
    }

    public void setPersonaje(String personaje) {
        this.personaje = personaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Manga{" +
                "id=" + id +
                ", autor='" + autor + '\'' +
                ", paginas=" + paginas +
                ", tags='" + tags + '\'' +
                ", parodia='" + parodia + '\'' +
                ", personaje='" + personaje + '\'' +
                ", titulo='" + titulo + '\'' +
                ", exp=" + exp +
                ", media=" + media +
                '}';
    }
}
