package source.page.fileBrowser.fileBrowserGetFolder.dto;

import source.common.dto.request.AbstractRequest;
import source.page.fileBrowser.dto.FolderConfigRequest;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
public class FileBrowserGetFolderRequest extends AbstractRequest{
	public FileBrowserGetFolderDto fileBrowserGetFolderDto = new FileBrowserGetFolderDto();
	public FolderConfigRequest folderConfigRequest = new FolderConfigRequest();
}
