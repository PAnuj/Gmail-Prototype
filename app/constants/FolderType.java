package constants;

public enum FolderType {
    INBOX("inbox"),
    SENT("sent"),
    TRASH("trash"),
    DRAFT("draft");

    public String name;

    FolderType(String name) {
        this.name = name;
    }

    public static FolderType getFoderTypeFromName(String name) {
        for(FolderType value : values()) {
            if(name == value.name) {
                return value;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }
}
