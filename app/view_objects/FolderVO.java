package view_objects;

import constants.FolderType;

import java.util.ArrayList;

public class FolderVO {
    public Integer itemCount;
    public Integer unreadItemCount;
    public Integer readItemCount;
    public FolderType folderType;
    public Integer mailId;
    public ArrayList<EmailItem> emailItems;
    public String emailId;
}
