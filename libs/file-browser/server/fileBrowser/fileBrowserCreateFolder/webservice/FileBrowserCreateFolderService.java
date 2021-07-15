package source.page.fileBrowser.fileBrowserCreateFolder.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.fileBrowser.fileBrowserCreateFolder.dto.FileBrowserCreateFolderRequest;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderResponse;
import source.page.fileBrowser.fileBrowserCreateFolder.process.FileBrowserCreateFolderProcess;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
@Path("/")
public class FileBrowserCreateFolderService extends AbstractWebService {

	@POST
	@Path("/FileBrowserCreateFolder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileBrowserGetFolderResponse product(FileBrowserCreateFolderRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (FileBrowserGetFolderResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new FileBrowserCreateFolderProcess(this);
	}
}

