
public abstract class GenericCSV {
 abstract void skipHeader();
 abstract GenericCSV prepareCSVObject(String [] line);
}
