package folderSizes;

public enum SortOption {    
    DEFAULT, NAME, SIZE;
    
    public String toString() {
        switch (this) {
            case NAME: return "Name";
            case SIZE: return "Size";
            default: return "Sort by...";
        }
    }
}
