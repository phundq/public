package source.page.fileBrowser.fileBrowserGetFile.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
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
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResultRowDto;

/**
 * Screen: File Browser Get File
 * Summary: display files (image) in current folder
 */
public class FileBrowserGetFileProcess extends AbstractProcess {

	public FileBrowserGetFileProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		FileBrowserGetFileRequest req = (FileBrowserGetFileRequest) request;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;

		if(!ValidateUtility.IsNull(req.folderConfigRequest.specTarget)) {
			getFileSpecTarget(dba, req, res);
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
		
		if (req.fileBrowserGetFileDto.currentFolder == null) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "Please provide folder path!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
		if (FolderConfigUseRole.AMIN.equals(req.folderConfigRequest.userRoleFv)) {
			getFileAdmin(dba, req, res);
			return res;
		}
		
		getFileOtherUser(dba, req, res);
		return res;
	}
	
	private boolean isImage(String type) {
		if(ValidateUtility.IsNull(type))
			return false;
		return (("png").equals(type.toLowerCase()) || ("jpg").equals(type.toLowerCase()) || ("jpeg").equals(type.toLowerCase()));
	}
	
	private void getFileAdmin(DBAccessor dba, FileBrowserGetFileRequest req, FileBrowserGetFileResponse res) throws ProcessCheckErrorException {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
			
			// check folder
			File folder = new File(rootFolderPath + imageSuffixPath + req.fileBrowserGetFileDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			// list childs of folder
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				String fileType = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1);
				if (listOfFiles[i].isFile() && isImage(fileType)) {
					FileBrowserGetFileResultRowDto fileItemRes = new FileBrowserGetFileResultRowDto();
					fileItemRes.name = listOfFiles[i].getName();

					String fileSubPath = (listOfFiles[i].getPath().substring(listOfFiles[i].getPath().indexOf(rootFolderPath) + rootFolderPath.length())).replace('\\', '/');

					fileItemRes.path = fileSubPath;
					fileItemRes.size = listOfFiles[i].length();
					fileItemRes.type = fileType;

					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					fileItemRes.date = dateFormat.format(listOfFiles[i].lastModified());

					byte[] imageData = Files.readAllBytes(listOfFiles[i].toPath());
					String base64Image = Base64.getEncoder().encodeToString(imageData);
					fileItemRes.data = base64Image;

					res.files.add(fileItemRes);
				}
			}

			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath().substring(folder.getPath().indexOf(rootFolderPath) + rootFolderPath.length());

		} catch (IOException e) {
			throw new ProcessCheckErrorException(e);
		}catch (DBException e) {
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
	
	private void getFileOtherUser(DBAccessor dba, FileBrowserGetFileRequest req, FileBrowserGetFileResponse res) {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String userUploadSuffixPath = configs.getConfigValueByName(ConfigNameContant.USER_UPLOAD_SUFFIX_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
			
			String userImageFolderPath =  rootFolderPath + imageSuffixPath + userUploadSuffixPath + "\\" + req.folderConfigRequest.storeCdFv;
			
			
			// check folder
			File folder = new File(userImageFolderPath + req.fileBrowserGetFileDto.currentFolder);
			if (!folder.exists() || !folder.isDirectory()) {
				ErrorDto errorDto = new ErrorDto();
				errorDto.controlID = "admin";
				errorDto.errMsg = "Folder is not exist!";
				List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
				errorDtos.add(errorDto);
				res.setFatalError(errorDtos);
				return;
			}

			// list childs of folder
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				String fileType = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1);
				if (listOfFiles[i].isFile() && isImage(fileType)) {
					FileBrowserGetFileResultRowDto fileItemRes = new FileBrowserGetFileResultRowDto();
					fileItemRes.name = listOfFiles[i].getName();

					String fileSubPath = (listOfFiles[i].getPath().substring(listOfFiles[i].getPath().indexOf(rootFolderPath) + rootFolderPath.length())).replace('\\', '/');

					fileItemRes.path = fileSubPath;
					fileItemRes.size = listOfFiles[i].length();
					fileItemRes.type = fileType;

					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					fileItemRes.date = dateFormat.format(listOfFiles[i].lastModified());

					byte[] imageData = Files.readAllBytes(listOfFiles[i].toPath());
					String base64Image = Base64.getEncoder().encodeToString(imageData);
					fileItemRes.data = base64Image;

					res.files.add(fileItemRes);
				}
			}

			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath()
					.substring(folder.getPath().indexOf(userImageFolderPath) + userImageFolderPath.length());
		
			
			

		} catch (IOException e) {
			// TODO: handle exception
		}catch (DBException e) {
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
	
	private void getFileSpecTarget(DBAccessor dba, FileBrowserGetFileRequest req, FileBrowserGetFileResponse res) throws ProcessCheckErrorException {
		try {
			ConfigRequest configRequest = new ConfigRequest();
			configRequest.configTarget = ConfigTargetConstant.SYSTEM;
			ConfigResponse configs;
			configs = ConfigService.getConfig(configRequest);

			String rootFolderPath = configs.getConfigValueByName(ConfigNameContant.FILE_BROWER_ROOT_FOLDER_PATH);
			String imageSuffixPath = configs.getConfigValueByName(ConfigNameContant.IMAGE_SUFFIX_PATH);
			
			String specTargetFolder = configs.getConfigValueByName(req.folderConfigRequest.specTarget);
			
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

			// list childs of folder
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				String fileType = listOfFiles[i].getName().substring(listOfFiles[i].getName().lastIndexOf(".") + 1);
				if (listOfFiles[i].isFile() && isImage(fileType)) {
					FileBrowserGetFileResultRowDto fileItemRes = new FileBrowserGetFileResultRowDto();
					fileItemRes.name = listOfFiles[i].getName();

					String fileSubPath = (listOfFiles[i].getPath().substring(listOfFiles[i].getPath().indexOf(rootFolderPath) + rootFolderPath.length())).replace('\\', '/');

					fileItemRes.path = fileSubPath;
					fileItemRes.size = listOfFiles[i].length();
					fileItemRes.type = fileType;

					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					fileItemRes.date = dateFormat.format(listOfFiles[i].lastModified());

					byte[] imageData = Files.readAllBytes(listOfFiles[i].toPath());
					String base64Image = Base64.getEncoder().encodeToString(imageData);
					fileItemRes.data = base64Image;

					res.files.add(fileItemRes);
				}
			}

			res.currentFolder.name = folder.getName();
			res.currentFolder.path = folder.getPath().substring(folder.getPath().indexOf(rootFolderPath) + rootFolderPath.length());

		} catch (IOException e) {
			throw new ProcessCheckErrorException(e);
		}catch (DBException e) {
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
		return new FileBrowserGetFileResponse();
	}
}
