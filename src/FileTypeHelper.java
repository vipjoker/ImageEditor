import java.io.File;

/**
 * Created by oleh on 13.04.18.
 */
public class FileTypeHelper {
    public static ImageType detectFileType(File file){

        String[] split = file.getAbsolutePath().split("\\.");
        if(split.length > 0){
            String fileType = split[split.length - 1];
            switch (fileType){
                case "jpg":
                    return ImageType.JPG;
                case "jpeg":
                    return ImageType.JPEG;
                case "png":
                    return ImageType.PNG;
                default:return  ImageType.NONE;

            }
        }
        return ImageType.NONE;
    }

    enum ImageType{
        PNG,JPG,JPEG,NONE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
