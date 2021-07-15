package source.page.fileBrowser.fileBrowserCreateFolder.dto;

import source.common.dto.request.AbstractRequest;
import source.page.fileBrowser.dto.FolderConfigRequest;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
public class FileBrowserCreateFolderRequest extends AbstractRequest{
	public FileBrowserCreateFolderDto fileBrowserCreateFolderDto = new FileBrowserCreateFolderDto();
	public FolderConfigRequest folderConfigRequest = new FolderConfigRequest();
}
