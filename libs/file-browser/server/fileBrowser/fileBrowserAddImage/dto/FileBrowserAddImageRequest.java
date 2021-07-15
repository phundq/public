package source.page.fileBrowser.fileBrowserAddImage.dto;

import source.common.dto.request.AbstractRequest;
import source.page.fileBrowser.dto.FolderConfigRequest;
import source.page.image.dto.ImageConfigDto;

/**
 * Screen: File Browser Add File
 * Summary: display files (image) in current folder after add new
 */
public class FileBrowserAddImageRequest extends AbstractRequest{
	public FileBrowserAddImageDto fileBrowserAddImageDto = new FileBrowserAddImageDto();
	public FolderConfigRequest folderConfigRequest = new FolderConfigRequest();
	public ImageConfigDto imageConfigDto;
}
