package source.page.imageSave.process;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
import source.common.utility.ValidateUtility;
import source.page.imageSave.dto.ImageSaveDto;
import source.page.imageSave.dto.ImageSaveRequest;
import source.page.imageSave.dto.ImageSaveResponse;

/**
 * Summary: Save image
 */
public class ImageSaveProcess extends AbstractProcess {
	public ImageSaveProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		ImageSaveRequest req = (ImageSaveRequest) request;
		ImageSaveDto reqDto = req.imageSaveDto;
		ImageSaveResponse res = (ImageSaveResponse) response;

		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);

			// Convert Base64 string image to normal image
			byte[] imageData = Base64.getDecoder().decode(reqDto.data);

			// Save image
			SimpleDateFormat sDF = new SimpleDateFormat("yyyyMMddhhmmssSSS");
			String imageName = sDF.format(new Date());
			String fullImagePath = rootFolderPath 
					+ imageSuffixPath + "\\"
					+ req.imageConfigDto.targetTypeSave
					+ "\\" + req.imageConfigDto.targetCd
					+ ((!ValidateUtility.IsNull(req.imageConfigDto.suffixPath)) ? ("\\" +req.imageConfigDto.suffixPath): "")
					+ "\\" + imageName + req.imageConfigDto.extension;

//			CHECK CREATE IF DIRECTORY NOT EXIST
			File directoryLevel1 = new File(rootFolderPath + imageSuffixPath);
			if (!directoryLevel1.exists()) {
				directoryLevel1.mkdir();
			}
			
			File directoryLevel2 = new File(rootFolderPath + imageSuffixPath + "\\" + req.imageConfigDto.targetTypeSave);
			if (!directoryLevel2.exists()) {
				directoryLevel2.mkdir();
			}
			
			File directoryLevel3 = new File(rootFolderPath + imageSuffixPath + "\\" + req.imageConfigDto.targetTypeSave + "\\" + req.imageConfigDto.targetCd);
			if (!directoryLevel3.exists()) {
				directoryLevel3.mkdir();
			}
			
			File directoryLevel4 = new File(rootFolderPath + imageSuffixPath + "\\" + req.imageConfigDto.targetTypeSave + "\\" + req.imageConfigDto.targetCd + ((!ValidateUtility.IsNull(req.imageConfigDto.suffixPath)) ? ("\\" +req.imageConfigDto.suffixPath): ""));
			if (!directoryLevel4.exists()) {
				directoryLevel4.mkdir();
			}

//			SAVE FILE
			File file = new File(fullImagePath);
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
			outputStream.write(imageData);

			File fileResult = new File(fullImagePath);
			if (fileResult.exists()) {
				res.imageSaveResultRowDto.name = fileResult.getName();
				res.imageSaveResultRowDto.size = fileResult.length();
				res.imageSaveResultRowDto.path = fileResult.getPath().replace(rootFolderPath, "").replace('\\', '/');
			}
		} catch (IOException e) {
			throw new FatalException(e);
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

		return res;
	}

	@Override
	protected AbstractResponse createNewResponse(AbstractRequest request) {
		return new ImageSaveResponse();
	}
}
