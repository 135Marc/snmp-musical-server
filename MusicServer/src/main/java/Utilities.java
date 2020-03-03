import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Utilities {

    public static void writeMusicMeta(MusicInfo mi) {
        Path ptext = Paths.get("Metadata/" + mi.getGenre() + ".meta");
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ptext.toString(),true))) {
            bw.write("{" + mi.getName() + ";");
            bw.write(mi.getArtist() + ";");
            bw.write(mi.getAlbum() + ";");
            bw.write(mi.getGenre() + ";");
            bw.write(mi.getYear() + ";");
            bw.write(mi.getDuration() + ";" );
            bw.write(mi.getPath() + "}" + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<MusicInfo> readMusicMeta() {
        List<MusicInfo> res = new ArrayList<>();
        File f = new File ("Metadata/");
        for (File fi : f.listFiles()) {
             if (fi.getName().contains(".meta")) {
                 try(BufferedReader br = Files.newBufferedReader(fi.toPath())) {
                         String line = br.readLine();
                         while (line!=null) {
                             String[] args = line.split(";");
                             args[0] = args[0].replace("{", "");
                             args[6] = args[6].replace("}", "");
                             MusicInfo musicinfo = new MusicInfo();
                             for (int i = 0; i < args.length; i++) {
                                 if (i == 0) musicinfo.setName(args[i]);
                                 else if (i == 1) musicinfo.setArtist(args[i]);
                                 else if (i == 2) musicinfo.setAlbum(args[i]);
                                 else if (i == 3) musicinfo.setGenre(args[i]);
                                 else if (i == 4) musicinfo.setYear(args[i]);
                                 else if (i == 5) musicinfo.setDuration(args[i]);
                                 else musicinfo.setPath(args[i]);
                             }
                             res.add(musicinfo);
                             line = br.readLine();
                         }
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }
             }
        }
        return res;
    }

    public static void populate_albumMap(List<String> results , Map<Integer,String> albumMap) {
        int half = Math.round(results.size()/2);
        List<String> albumIDs = results.subList(0,half);
        List<String> albumNames = results.subList(half,results.size());
        for (int i=0;i<results.size()/2;i++) {
            albumMap.put(Integer.valueOf(albumIDs.get(i)),albumNames.get(i));
        }
    }

    public static void populate_artistMap(List<String> results,Map<Integer,String> artistMap) {
        int half = Math.round(results.size()/2);
        List<String> artistIDs = results.subList(0,half);
        List<String> artistNames = results.subList(half,results.size());
        for (int i=0;i<results.size()/2;i++) {
            artistMap.put(Integer.valueOf(artistIDs.get(i)),artistNames.get(i));
        }
    }

    public static void populate_genreMap(List<String> results, Map<Integer,String> genreMap) {
        int half = Math.round(results.size()/2);
        List<String> genreIDs = results.subList(0,half);
        List<String> genreNames = results.subList(half,results.size());
        for (int i=0;i<results.size()/2;i++) {
            genreMap.put(Integer.valueOf(genreIDs.get(i)),genreNames.get(i));
        }
    }

    public static ObservableList<PieChart.Data> generatePieMap(List<MusicInfo> musicList) {
        ObservableList<PieChart.Data> datalist = FXCollections.observableArrayList();
        Set<String> genreList = musicList.stream().map(MusicInfo::getGenre).collect(Collectors.toSet());
        for (int i=0;i<genreList.size();i++) {
            String genreName = genreList.iterator().next();
            int genreFilt = (int) musicList.stream().filter(s -> s.getGenre().equals(genreName)).count();
            PieChart.Data d1 = new PieChart.Data(genreName,genreFilt);
            datalist.add(d1);
        }
        return datalist;
    }

}
