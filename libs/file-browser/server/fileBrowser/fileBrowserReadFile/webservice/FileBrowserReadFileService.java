package source.page.fileBrowser.fileBrowserReadFile.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import source.common.config.dto.ConfigRequest;
import source.common.config.dto.ConfigResponse;
import source.common.config.dto.ConfigResultRowDto.ConfigNameContant;
import source.common.config.dto.ConfigResultRowDto.ConfigTargetConstant;
import source.common.config.service.ConfigService;

/**
 * Screen: File reader
 * Summary: display image and file
 */
@Path("/")
public class FileBrowserReadFileService {

	/**
	 * Function: File reader
	 * Summary: display file
	 */
	@GET
	@Path("/File/{path: .*}")
	public Response  getFile(@PathParam("path") String path) throws FileNotFoundException {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs = ConfigService.getConfig(configRequest);

			String pathRootString = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);

			File file = new File(pathRootString + "/" + path);
			FileInputStream fis = null;

			if (!file.exists()) {
				return Response.status(404).build();
			}
			fis = new FileInputStream(file);

			String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if (("jfif").equals(fileType.toLowerCase()) || ("png").equals(fileType.toLowerCase()) || ("jpg").equals(fileType.toLowerCase())
					|| ("jpeg").equals(fileType.toLowerCase()))
				return Response.status(Status.OK).entity(fis).header("Content-Type", "image/png")
						.header("Content-disposition", "inline; filename=" + URLEncoder.encode(file.getName(), "UTF-8"))
						.build();
			if (("mp4").equals(fileType.toLowerCase()) || ("ogg").equals(fileType.toLowerCase()) || ("webm").equals(fileType.toLowerCase()))
				return Response.status(Status.OK).entity(fis).header("Content-Type", "video/" + fileType.toLowerCase())
						.header("Content-disposition", "inline; filename=" + URLEncoder.encode(file.getName(), "UTF-8"))
						.build();
			return Response.status(Status.OK).entity(fis).header("Content-Type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"))
					.build();

		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	/**
	 * Function: Image reader
	 * Summary: display image only
	 */
	@SuppressWarnings("resource")
	@GET
	@Path("/Image/{path: .*}")
	public Response  getImage(@PathParam("path") String path) throws FileNotFoundException {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs = ConfigService.getConfig(configRequest);

			String pathRootString = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);

			File file = new File(pathRootString + "/" + path);
			FileInputStream fis = null;

			if (!file.exists()) {
				return Response.status(404).build();
			}
			fis = new FileInputStream(file);

			String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if (("jfif").equals(fileType.toLowerCase()) || ("png").equals(fileType.toLowerCase()) || ("jpg").equals(fileType.toLowerCase())
					|| ("jpeg").equals(fileType.toLowerCase()))
				return Response.status(Status.OK).entity(fis).header("Content-Type", "image/png")
						.header("Content-disposition", "inline; filename=" + URLEncoder.encode(file.getName(), "UTF-8"))
						.build();
			return Response.status(404).build();

		} catch (Exception e) {
			return Response.status(500).build();
		}
	}
}

