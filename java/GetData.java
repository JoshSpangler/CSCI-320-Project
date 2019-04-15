import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Gets data from text files
 *
 * @author Chris Murphy
 */

public class GetData {

    private static final String SELECT_ALL = "*";

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
    public static String[] getAttribute(String[] contents, String brand, String model, String series, String design,
                                        String attrib, int index, boolean sort){
        ArrayList<String> attribute=new ArrayList<>();
        for(String line:contents){
            if((!brand.equals(SELECT_ALL) && !line.contains(brand+",")) || (!model.equals(SELECT_ALL) &&
                    !line.contains(model)) || (!series.equals(SELECT_ALL) && !line.contains(series)) ||
                    (!design.equals(SELECT_ALL) && !line.contains(design))){
                continue;
            }
            String[] parsedLine=line.split(",")[index].split("/");
            for(String parsed:parsedLine) {
                if (!attribute.contains(parsed)) {
                    attribute.add(parsed);
                }
            }
        }
        if(sort) {
            Collections.sort(attribute);
        }
        if(!attrib.equals("Upgrade")){
            attribute.add(0, attrib);
        }
        else{
            for(int i=attribute.size()-1; i>=0; i--){
                if(i>0){
                    if(attribute.get(i).split(":")[0].equals(attribute.get(i-1).split(":")[0])){
                        attribute.remove(i);
                    }
                }
            }
        }
        return attribute.toArray(String[]::new);
    }
}
