package norsecommunityplugin.norsecommunityplugin.folders;

import java.io.File;

public class DataFolder {

    private String FolderName;
    private String Path;

    public DataFolder(String folderName, String path){

        this.FolderName = folderName;
        this.Path = path;

        createDataFolder();


    }

    public void createDataFolder(){
         File pluginsFolder = new File(Path);
         File dataFolder = new File(pluginsFolder, FolderName);

            if (!dataFolder.exists()){
                dataFolder.mkdir();
            }



    }






}
