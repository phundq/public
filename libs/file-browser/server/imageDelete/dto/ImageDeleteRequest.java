package source.page.imageDelete.dto;

/**
 * Summary: Delete image
 */
import source.common.dto.request.AbstractRequest;
import source.page.image.dto.ImageConfigDto;

public class ImageDeleteRequest extends AbstractRequest{
	public ImageDeleteDto imageDeleteDto = new ImageDeleteDto();
	public ImageConfigDto imageConfigDto = new ImageConfigDto();
}
