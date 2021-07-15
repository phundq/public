package source.page.fileBrowser.fileBrowserCreateFolder.process;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import source.common.ILogSender;
import source.common.config.dto.ConfigRequest;
import source.common.config.dto.ConfigResponse;
import source.common.config.dto.ConfigResultRowDto.ConfigNameContant;
import source.common.config.dto.ConfigResultRowDto.ConfigTargetConstant;
import source.common.config.service.ConfigService;
import source.common.database.DBAccessor;
import source.common.dto.ErrorDto;
import source.common.dto.request.AbstractRequest;
import source.common.dto.response.AbstractResponse;
import source.common.exception.DBException;
import source.common.exception.FatalException;
import source.common.exception.ProcessCheckErrorException;
import source.common.process.AbstractProcess;
import source.common.utility.ValidateUtility;
import source.page.fileBrowser.dto.FolderConfigRequest.FolderConfigUseRole;
import source.page.fileBrowser.fileBrowserCreateFolder.dto.FileBrowserCreateFolderRequest;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderRequest;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderResponse;
import source.page.fileBrowser.fileBrowserGetFolder.process.FileBrowserGetFolderProcess;

/**
 * Screen: File Browser Get Folder 
 * Summary: display current folder and nearest childs
 */
public class FileBrowserCreateFolderProcess extends AbstractProcess {

	public FileBrowserCreateFolderProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		FileBrowserCreateFolderRequest req = (FileBrowserCreateFolderRequest) request;
		FileBrowserGetFolderResponse res = (FileBrowserGetFolderResponse) response;
		// validation
		if (req.fileBrowserCreateFolderDto.currentFolder == null) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "Please provide folder path!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
		if (ValidateUtility.IsNull(req.fileBrowserCreateFolderDto.newFolderName)) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "Please provide new folder name!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}


		// Add new Folder
		if (FolderConfigUseRole.AMIN.equals(req.folderConfigRequest.userRoleFv)) {
			createFolderRoleAdmin(dba, req, res);
			if (res.getFatalError().size() <= 0 && res.getNormalError().size() <= 0)
				setFolderResponse(dba, req, res, parentResponse);
			return res;
		}

		createFolderRoleOther(dba, req, res);
		if (res.getFatalError().size() <= 0 && res.getNormalError().size() <= 0)
			setFolderResponse(dba, req, res, parentResponse);
		return res;
	}
	
	private void setFolderResponse(DBAccessor dba, FileBrowserCreateFolderRequest req, FileBrowserGetFolderResponse res, AbstractResponse parentResponse) {
		try {
			FileBrowserGetFolderRequest fileBrowserGetFolderRequest = new FileBrowserGetFolderRequest();
			fileBrowserGetFolderRequest.access = req.access;
			fileBrowserGetFolderRequest.folderConfigRequest = req.folderConfigRequest;
			fileBrowserGetFolderRequest.fileBrowserGetFolderDto.currentFolder = req.fileBrowserCreateFolderDto.currentFolder;
			
			FileBrowserGetFolderProcess fileBrowserGetFolderProcess = new FileBrowserGetFolderProcess(this);
			
			res = (FileBrowserGetFolderResponse) fileBrowserGetFolderProcess.process(dba, fileBrowserGetFolderRequest, res, parentResponse);
		} catch (FatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessCheckErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createFolderRoleAdmin(DBAccessor dba, FileBrowserCreateFolderRequest req, FileBrowserGetFolderResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			// check folder

			File folder = new File(rootFolderPath + req.fileBrowserCreateFolderDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			// generate new folder for user
			File newDirectory = new File(rootFolderPath + req.fileBrowserCreateFolderDto.currentFolder + "\\"
					+ req.fileBrowserCreateFolderDto.newFolderName.trim());
			if (!newDirectory.exists()) {
				newDirectory.mkdir();
			} else {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder with name " + req.fileBrowserCreateFolderDto.newFolderName + " existed!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setNormalError(errorDtos);
				return;
			}
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
	}
	
	private void createFolderRoleOther(DBAccessor dba, FileBrowserCreateFolderRequest req,
			FileBrowserGetFolderResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String userUploadSuffixPath = configs.getConfigValueByName(ConfigNameContant.USER_UPLOAD_SUFFIX_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);

			String userImageFolderPath = rootFolderPath + imageSuffixPath + userUploadSuffixPath + "\\"
					+ req.folderConfigRequest.storeCdFv;
			
			
			// check folder
			File folder = new File(userImageFolderPath + req.fileBrowserCreateFolderDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			// generate new folder for user
			File newDirectory = new File(userImageFolderPath + req.fileBrowserCreateFolderDto.currentFolder + "\\" + req.fileBrowserCreateFolderDto.newFolderName.trim());
			if (!newDirectory.exists()) {
				newDirectory.mkdir();
			}else {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder with name "+ req.fileBrowserCreateFolderDto.newFolderName +" existed!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setNormalError(errorDtos);
				return;
			}

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
	}
	

	@Override
	protected AbstractResponse createNewResponse(AbstractRequest request) {
		return new FileBrowserGetFolderResponse();
	}
}
