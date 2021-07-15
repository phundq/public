import { ImageConfigDto, ReqImageSaveDto } from '../../../common/image';
import { ReqFolderConfig } from './req-folder-config';
import { Req } from '../../../common/request';
export interface ReqFileBrowserDeleteImageDto {
    currentFolder: string;
    imageDeleteDtos: ReqImageSaveDto[];
}

export interface ReqFileBrowserDeleteImage extends Req {
    fileBrowserDeleteImageDto: ReqFileBrowserDeleteImageDto;
    folderConfigRequest: ReqFolderConfig;
    imageConfigDto: ImageConfigDto;
}