package folderSizes;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.TreeItem;

import static java.lang.Math.*;

/**
 * Constructs a tree representation of the file system using a given File as 
 * the root.
 * 
 * Also calculates the sizes for every file and directory in the tree.
 */
public final class FileTree
{
    private final Path file;
    private final long size;
    private final List<FileTree> children = new LinkedList<>();
        
    public FileTree(Path file) throws IOException
    {
        this.file = file;
        if (Files.isReadable(file) && Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS))
        {
            DirectoryStream<Path> dir = Files.newDirectoryStream(file);
            long size = 0;
            for (Path p : dir)
            {
                FileTree child = new FileTree(p);
                this.children.add(child);
                size += child.size;
            }
            this.size = size;
            dir.close();
        }
        else
            this.size = Files.size(file);
    }

    /**
     * Converts the tree to a TreeItem for use within JavaFX
     * 
     * @return A TreeItem of Strings representing the tree
     */
    public TreeItem<String> toTreeItem()
    {
        TreeItem<String> tree = new TreeItem<>(this.toString());
        children.forEach(f -> tree.getChildren().add(f.toTreeItem()));
        return tree;
    }
        
    public void sortBy(SortOption option)
    {
        switch (option)
        {
            case NAME: children.sort((f1, f2) -> f1.file.compareTo(f2.file)); break;
            case SIZE: children.sort((f1, f2) -> Long.compare(f2.size, f1.size)); break;
            default: return;
        }
        children.forEach(f -> f.sortBy(option));
    }
        
    /**
     * Returns a string representation of the root for this FileTree in 
     * the form "Filename: size"
     * 
     * @return The String representation.
    */
    @Override
    public String toString()
    {
        return file.getFileName() + ": " + this.sizeAsString();
    }

    /**
     * Converts the long file size into a more user readable String
     * representation
     * 
     * @return String representation of the file size e.g. 3.2GB
     */
    private String sizeAsString()
    {
        int exponent = (int)(log(size) / log(1024));
        double roundedDouble = round(size/ pow(1024, exponent) * 100) / 100.0;
        String magnitude;
        switch (exponent)
        {
            default : magnitude = "bytes"; break;
            case 1: magnitude = "KB"; break;
            case 2: magnitude = "MB"; break;
            case 3: magnitude = "GB"; break;
            case 4: magnitude = "TB"; break;
        }
        return roundedDouble + " " + magnitude;
    }
}