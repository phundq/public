package source.page.imageSearch.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.imageSearch.dto.ImageSearchRequest;
import source.page.imageSearch.dto.ImageSearchResponse;
import source.page.imageSearch.process.ImageSearchProcess;

/**
 * Summary: Search image
 */
@Path("/")
public class ImageSearchWebService extends AbstractWebService {

	@POST
	@Path("/ImageSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ImageSearchResponse ImageSearch(ImageSearchRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (ImageSearchResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new ImageSearchProcess(this);
	}
}