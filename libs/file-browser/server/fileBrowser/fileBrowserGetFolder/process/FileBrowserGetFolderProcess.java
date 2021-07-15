package source.page.fileBrowser.fileBrowserGetFolder.process;

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
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderRequest;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderResponse;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderResultRowDto;

/**
 * Screen: File Browser Get Folder Summary: display current folder and nearest
 * childs
 */
public class FileBrowserGetFolderProcess extends AbstractProcess {

	public FileBrowserGetFolderProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		FileBrowserGetFolderRequest req = (FileBrowserGetFolderRequest) request;
		FileBrowserGetFolderResponse res = (FileBrowserGetFolderResponse) response;

		if(!ValidateUtility.IsNull(req.folderConfigRequest.specTarget)) {
			getFolderSpecTarget(dba, req, res);
			return res;
		}
		
		// validation
		if (req.folderConfigRequest == null || ValidateUtility.IsNull(req.folderConfigRequest.userRoleFv)) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "user config not found!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
		if (req.fileBrowserGetFolderDto.currentFolder == null) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "Please provide folder path!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
		if (FolderConfigUseRole.AMIN.equals(req.folderConfigRequest.userRoleFv)) {
			getFolderAdmin(dba, req, res);
			return res;
		}
		
		getFolderOtherUser(dba, req, res);
		return res;

	}

	private boolean hasChilds(File file) {
		File[] listOfFiles = file.listFiles();
		if (listOfFiles.length <= 0)
			return false;
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println("Directory " + listOfFiles[i].getName());
			if (listOfFiles[i].isDirectory()) {
				return true;
			}
		}
		return false;
	}

	private void getFolderAdmin(DBAccessor dba, FileBrowserGetFolderRequest req, FileBrowserGetFolderResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
			String adminImageFolderPath =  rootFolderPath + imageSuffixPath;
			// check folder
			File folder = new File(adminImageFolderPath + req.fileBrowserGetFolderDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					FileBrowserGetFolderResultRowDto folderItemRes = new FileBrowserGetFolderResultRowDto();

					File subFolder = new File(listOfFiles[i].getPath());

					folderItemRes.hasChild = hasChilds(subFolder);
					folderItemRes.path = subFolder.getPath()
							.substring(subFolder.getPath().indexOf(adminImageFolderPath) + adminImageFolderPath.length());
					folderItemRes.name = subFolder.getName();

					res.folders.add(folderItemRes);
				}
			}
			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath()
					.substring(folder.getPath().indexOf(adminImageFolderPath) + adminImageFolderPath.length());

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
	
	private void getFolderOtherUser(DBAccessor dba, FileBrowserGetFolderRequest req, FileBrowserGetFolderResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String userUploadSuffixPath = configs.getConfigValueByName(ConfigNameContant.USER_UPLOAD_SUFFIX_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
			
			String userImageFolderPath =  rootFolderPath + imageSuffixPath + userUploadSuffixPath + "\\" + req.folderConfigRequest.storeCdFv;
			
			// generate root folder for user
			File directoryLevel1 = new File(rootFolderPath + imageSuffixPath + userUploadSuffixPath);
			if (!directoryLevel1.exists()) {
				directoryLevel1.mkdir();
			}
			File directoryLevel2 = new File(rootFolderPath + imageSuffixPath + userUploadSuffixPath + "\\" + req.folderConfigRequest.storeCdFv);
			if (!directoryLevel2.exists()) {
				directoryLevel2.mkdir();
			}
			File directoryLevel3 = new File(rootFolderPath + imageSuffixPath + userUploadSuffixPath + "\\" + req.folderConfigRequest.storeCdFv + imageSuffixPath);
			if (!directoryLevel3.exists()) {
				directoryLevel3.mkdir();
			}
			
			// check folder
			File folder = new File(userImageFolderPath + req.fileBrowserGetFolderDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					FileBrowserGetFolderResultRowDto folderItemRes = new FileBrowserGetFolderResultRowDto();

					File subFolder = new File(listOfFiles[i].getPath());

					folderItemRes.hasChild = hasChilds(subFolder);
					folderItemRes.path = subFolder.getPath()
							.substring(subFolder.getPath().indexOf(userImageFolderPath) + userImageFolderPath.length());
					folderItemRes.name = subFolder.getName();

					res.folders.add(folderItemRes);
				}
			}
			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath()
					.substring(folder.getPath().indexOf(userImageFolderPath) + userImageFolderPath.length());

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
	
	private void getFolderSpecTarget(DBAccessor dba, FileBrowserGetFolderRequest req, FileBrowserGetFolderResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
		
			String specTargetFolder = configs.getConfigValueByName(ConfigNameContant.USER_UPLOAD_SUFFIX_PATH);
			
			// check folder
			File folder = new File(rootFolderPath + imageSuffixPath + specTargetFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

//			File[] listOfFiles = folder.listFiles();
//			for (int i = 0; i < listOfFiles.length; i++) {
//				if (listOfFiles[i].isDirectory()) {
//					FileBrowserGetFolderResultRowDto folderItemRes = new FileBrowserGetFolderResultRowDto();
//
//					File subFolder = new File(listOfFiles[i].getPath());
//
//					folderItemRes.hasChild = hasChilds(subFolder);
//					folderItemRes.path = subFolder.getPath()
//							.substring(subFolder.getPath().indexOf(rootFolderPath + imageSuffixPath + specTargetFolder) + (rootFolderPath + imageSuffixPath + specTargetFolder).length());
//					folderItemRes.name = subFolder.getName();
//
//					res.folders.add(folderItemRes);
//				}
//			}
			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath()
					.substring(folder.getPath().indexOf(rootFolderPath + imageSuffixPath + specTargetFolder) + (rootFolderPath + imageSuffixPath + specTargetFolder).length());

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
