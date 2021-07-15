package source.page.fileBrowser.fileBrowserGetFile.dto;

import source.common.dto.request.AbstractRequest;
import source.page.fileBrowser.dto.FolderConfigRequest;

/**
 * Screen: File Browser Get File
 * Summary: display files (image) in current folder
 */
public class FileBrowserGetFileRequest extends AbstractRequest{
	public FileBrowserGetFileDto fileBrowserGetFileDto = new FileBrowserGetFileDto();
	public FolderConfigRequest folderConfigRequest = new FolderConfigRequest();
}
