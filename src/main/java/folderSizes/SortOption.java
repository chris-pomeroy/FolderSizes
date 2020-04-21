package folderSizes;

enum SortOption 
{    
    DEFAULT, NAME, SIZE;
    
    @Override
    public String toString()
    {
        switch (this)
        {
            case NAME: return "Name";
            case SIZE: return "Size";
            default: return "Sort by...";
        }
    }
}
