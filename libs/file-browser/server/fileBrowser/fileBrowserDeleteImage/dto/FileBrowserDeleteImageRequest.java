package source.page.fileBrowser.fileBrowserDeleteImage.dto;

import source.common.dto.request.AbstractRequest;
import source.page.fileBrowser.dto.FolderConfigRequest;
import source.page.image.dto.ImageConfigDto;

/**
 * Screen: File Browser Delete File
 * Summary: display files (image) in current folder after delete
 */
public class FileBrowserDeleteImageRequest extends AbstractRequest{
	public FileBrowserDeleteImageDto fileBrowserDeleteImageDto = new FileBrowserDeleteImageDto();
	public FolderConfigRequest folderConfigRequest = new FolderConfigRequest();
	public ImageConfigDto imageConfigDto;
}
