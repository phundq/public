package source.page.imageDelete.webservice;

/**
 * Summary: Delete image
 */
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.imageDelete.dto.ImageDeleteRequest;
import source.page.imageDelete.dto.ImageDeleteResponse;
import source.page.imageDelete.process.ImageDeleteProcess;

@Path("/")
public class ImageDeleteWebService extends AbstractWebService {

	@POST
	@Path("/ImageDelete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ImageDeleteResponse ImageDelete(ImageDeleteRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (ImageDeleteResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new ImageDeleteProcess(this);
	}
}