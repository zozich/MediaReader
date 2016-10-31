import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        extractMediaInfo("C:\\Users\\zozic\\Downloads\\TrackAnalyzer\\trackanalyzer", "Kizomba.txt");
//        extractMediaInfo("C:\\Users\\zozic\\Desktop\\Music\\Kizomba", "Kizomba.txt");
//        extractMediaInfo("C:\\Users\\zozic\\Desktop\\Music\\Bachata", "Bachata.txt");
//        extractMediaInfo("C:\\Users\\zozic\\Desktop\\Music\\Salsa", "Salsa.txt");
    }

    private static void extractMediaInfo(String dirName, String outputFileName) throws IOException {
        File output = new File(outputFileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));

        File directory = new File(dirName);
        for (File track : directory.listFiles()) {
            if (track.getName().endsWith(".mp3")) {
                int rating = 9999999;
                try {
                    MP3File f = (org.jaudiotagger.audio.mp3.MP3File) AudioFileIO.read(track);
                    AbstractID3v2Frame frame = f.getID3v2Tag().getFirstField("POPM");
                    rating = Integer.parseInt(frame.getBody().getObject("Rating").toString()) / 64 + 1;
                    writer.write(track.getName().substring(0, track.getName().length() - 4) + ";" + rating);

                    try {
                        frame = f.getID3v2Tag().getFirstField("TBPM");
                        Double bpm = Double.parseDouble(frame.getBody().getObject("Text").toString());
                        bpm = bpm > 120 ? bpm / 2 : bpm;
                        Long bpmLong = Math.round(bpm);
                        writer.write(";" + bpmLong.toString());
                    } catch (Exception ignore) { }

                    writer.newLine();
                } catch (Exception e) {
                    writer.write(track.getName() + ";" + rating);
                    writer.newLine();
                }
            }
        }
        writer.close();
    }
}
