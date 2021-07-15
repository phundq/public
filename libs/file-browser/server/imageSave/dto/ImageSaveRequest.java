package source.page.imageSave.dto;

import source.common.dto.request.AbstractRequest;
import source.page.image.dto.ImageConfigDto;

/**
 * Summary: Save image
 */
public class ImageSaveRequest extends AbstractRequest{
	public ImageSaveDto imageSaveDto = new ImageSaveDto();
	public ImageConfigDto imageConfigDto = new ImageConfigDto();
}
