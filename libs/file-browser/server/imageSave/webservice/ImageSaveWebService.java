package source.page.imageSave.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.imageSave.dto.ImageSaveRequest;
import source.page.imageSave.dto.ImageSaveResponse;
import source.page.imageSave.process.ImageSaveProcess;

/**
 * Summary: Save image
 */
@Path("/")
public class ImageSaveWebService extends AbstractWebService {

	@POST
	@Path("/ImageSave")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ImageSaveResponse ImageSave(ImageSaveRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (ImageSaveResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new ImageSaveProcess(this);
	}
}