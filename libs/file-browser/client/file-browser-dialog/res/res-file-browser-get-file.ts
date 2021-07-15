import { Res } from '../../../common/response';
export interface CurrentFolder {
    name: string;
    path: string;
}

export interface ResFileBrowserGetFileDto {
    name: string;
    path: string;
    data: string;
    size: number;
    date: string;
}

export interface ResFileBrowserGetFile extends Res {
    currentFolder: CurrentFolder;
    files: ResFileBrowserGetFileDto[];
}
