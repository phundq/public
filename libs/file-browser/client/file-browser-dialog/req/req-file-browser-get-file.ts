import { ReqFolderConfig } from './req-folder-config';
import { Req } from '../../../common/request';
export interface ReqFileBrowserGetFileDto {
    currentFolder: string;
}

export interface ReqFileBrowserGetFile extends Req {
    fileBrowserGetFileDto: ReqFileBrowserGetFileDto;
    folderConfigRequest: ReqFolderConfig
}