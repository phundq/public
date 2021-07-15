package source.page.fileBrowser.fileBrowserAddImage.dto;

import java.util.ArrayList;

import source.common.dto.AbstractDto;
import source.page.imageSave.dto.ImageSaveDto;

/**
 * Screen: File Browser Add File
 * Summary: display files (image) in current folder after add new
 */
public class FileBrowserAddImageDto extends AbstractDto {
	public String currentFolder;
	public ArrayList<ImageSaveDto> imageSaveDtos = new ArrayList<ImageSaveDto>();
}
