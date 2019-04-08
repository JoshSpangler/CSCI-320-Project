import java.io.*;
import java.util.ArrayList;

public class GetData {

    /**
     * Reads a file for the dealer's orfer
     * @param filename the file to read from
     * @return an array of strings from the file
     * @throws FileNotFoundException if the file was not found
     * @throws IOException if the file cannot be read
     */
    public static String[] readFile(String filename) {
        try {
            File file = new File(filename);
            BufferedReader bf = new BufferedReader(new FileReader(file));
            ArrayList<String> lines = new ArrayList();
            String line = "";
            while ((line = bf.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(String[]::new);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a list of the unique individual attributes available to the dealer
     * @param contents the file contents
     * @param index the index of the content that we want
     * @return an array of string attributes from the file
     */
    public static String[] getAttribute(String[] contents, String attrib, int index){
        ArrayList<String> attribute=new ArrayList<>();
        for(String line:contents){
            String[] parsedLine=line.split(",")[index].split("/");
            for(String parsed:parsedLine) {
                if (!attribute.contains(parsed)) {
                    attribute.add(parsed);
                }
            }
        }
        return attribute.toArray(String[]::new);
    }
}
