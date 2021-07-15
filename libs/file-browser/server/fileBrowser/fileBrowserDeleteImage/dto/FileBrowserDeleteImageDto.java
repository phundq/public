package source.page.fileBrowser.fileBrowserDeleteImage.dto;

import java.util.ArrayList;

import source.common.dto.AbstractDto;
import source.page.imageSave.dto.ImageSaveDto;

/**
 * Screen: File Browser Delete File
 * Summary: display files (image) in current folder after delete
 */
public class FileBrowserDeleteImageDto extends AbstractDto {
	public String currentFolder;
	public ArrayList<ImageSaveDto> imageDeleteDtos = new ArrayList<ImageSaveDto>();
}
