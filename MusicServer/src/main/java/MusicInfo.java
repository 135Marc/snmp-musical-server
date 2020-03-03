public class MusicInfo {

    private String name;
    private String artist;
    private String album;
    private String genre;
    private String year;
    private String duration;
    private String path;

    public MusicInfo() {
        this.name=this.artist=this.album=this.genre=this.year=this.duration="n/a";
    }

    public MusicInfo(String name, String artist, String album, String genre, String year, String duration,String path) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.path=path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append(name)
                .append(";")
                .append(artist)
                .append(";")
                .append(album)
                .append(";")
                .append(genre)
                .append( ";")
                .append(year)
                .append(";")
                .append(duration)
                .append(";")
                .append(path)
                .append("}");
        return sb.toString();
    }
}
