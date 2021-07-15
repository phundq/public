package source.page.imageDelete.process;

/**
 * Summary: Delete image
 */
import java.io.File;

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
import source.page.imageDelete.dto.ImageDeleteDto;
import source.page.imageDelete.dto.ImageDeleteRequest;
import source.page.imageDelete.dto.ImageDeleteResponse;

public class ImageDeleteProcess extends AbstractProcess {

	public ImageDeleteProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		ImageDeleteRequest req = (ImageDeleteRequest) request;
		ImageDeleteDto reqDto = req.imageDeleteDto;
		ImageDeleteResponse res = (ImageDeleteResponse) response;

		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			
			// Delete image
			String pathFolder = rootFolderPath + "/" + reqDto.imagePathFv;
			File file = new File(pathFolder);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	protected AbstractResponse createNewResponse(AbstractRequest request) {
		return new ImageDeleteResponse();
	}
}
