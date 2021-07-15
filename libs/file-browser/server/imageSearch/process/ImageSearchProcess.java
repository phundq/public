package source.page.imageSearch.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Base64;

import source.common.ILogSender;
import source.common.config.dto.ConfigRequest;
import source.common.config.dto.ConfigResponse;
import source.common.config.dto.ConfigResultRowDto.ConfigNameContant;
import source.common.config.dto.ConfigResultRowDto.ConfigTargetConstant;
import source.common.config.service.ConfigService;
import source.common.database.DBAccessor;
import source.common.dto.request.AbstractRequest;
import source.common.dto.response.AbstractResponse;
import source.common.exception.DBException;
import source.common.exception.FatalException;
import source.common.exception.ProcessCheckErrorException;
import source.common.process.AbstractProcess;
import source.page.imageSearch.dto.ImageSearchDto;
import source.page.imageSearch.dto.ImageSearchRequest;
import source.page.imageSearch.dto.ImageSearchResponse;
import source.page.imageSearch.dto.ImageSearchResultRowDto;

/**
 * Summary: Search image
 */
public class ImageSearchProcess extends AbstractProcess {

	public ImageSearchProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		ImageSearchRequest req = (ImageSearchRequest) request;
		ImageSearchDto reqDto = req.imageSearchDto;
		ImageSearchResponse res = (ImageSearchResponse) response;

		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			
			// read image from folder
			String pathFolder = rootFolderPath + "/" + reqDto.imagePathFv;
			File file = new File(pathFolder);
			if (!file.exists()) {
				res.result = null;
    			return res;
    		}
			byte[] imageData = Files.readAllBytes(file.toPath());
			String base64Image = Base64.getEncoder().encodeToString(imageData);

			// response
			ImageSearchResultRowDto resDto = new ImageSearchResultRowDto();
			resDto.name = file.getName();
			resDto.size = file.length();
			resDto.data = base64Image;
			res.result = resDto;

		} catch (IOException e) {
			throw new ProcessCheckErrorException(e);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected AbstractResponse createNewResponse(AbstractRequest request) {
		return new ImageSearchResponse();
	}

}
