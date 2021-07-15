package source.page.imageSearch.dto;

import source.common.dto.request.AbstractRequest;
import source.page.image.dto.ImageConfigDto;

/**
 * Summary: Search image
 */
public class ImageSearchRequest extends AbstractRequest{
	public ImageSearchDto imageSearchDto = new ImageSearchDto();
	public ImageConfigDto imageConfigDto = new ImageConfigDto();
}
