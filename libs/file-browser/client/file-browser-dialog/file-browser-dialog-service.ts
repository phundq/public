import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppInitService } from 'src/app/app-initializer';
import { CommonService } from 'src/app/common/common-service';
import { ApiUrl } from './../../common/const';
import { ReqFileBrowserAddImage } from './req/req-file-browser-add-image';
import { ReqFileBrowserCreateFolder } from './req/req-file-browser-create-folder';
import { ReqFileBrowserDeleteImage } from './req/req-file-browser-delete-image';
import { ReqFileBrowserGetFile } from './req/req-file-browser-get-file';
import { ReqFileBrowserGetFolder } from './req/req-file-browser-get-folder';
import { ResFileBrowserGetFile } from './res/res-file-browser-get-file';
import { ResFileBrowserGetFolder } from './res/res-file-browser-get-folder';

@Injectable()
export class FileBrowserService {
    constructor(
        private http: HttpClient,
        private commonService: CommonService,
        private appInitializer: AppInitService

    ) {
    }

    getFolders(reqFileBrowserGetFolder: ReqFileBrowserGetFolder): Observable<ResFileBrowserGetFolder> {
        return this.http.post<ResFileBrowserGetFolder>(this.appInitializer.config.apiUrl + ApiUrl.FILE_BROWSER_GET_FOLDER, reqFileBrowserGetFolder);
    }
    createFolder(reqFileBrowserCreateFolder: ReqFileBrowserCreateFolder): Observable<ResFileBrowserGetFolder> {
        return this.http.post<ResFileBrowserGetFolder>(this.appInitializer.config.apiUrl + ApiUrl.FILE_BROWSER_CREATE_FOLDER, reqFileBrowserCreateFolder);
    }

    getFiles(reqFileBrowserGetFile: ReqFileBrowserGetFile): Observable<ResFileBrowserGetFile> {
        return this.http.post<ResFileBrowserGetFile>(this.appInitializer.config.apiUrl + ApiUrl.FILE_BROWSER_GET_FILE, reqFileBrowserGetFile);
    }

    addImages(reqFileBrowserAddImage: ReqFileBrowserAddImage): Observable<ResFileBrowserGetFile> {
        return this.http.post<ResFileBrowserGetFile>(this.appInitializer.config.apiUrl + ApiUrl.FILE_BROWSER_ADD_IMAGE, reqFileBrowserAddImage);
    }

    deleteImages(reqFileBrowserDeleteImage: ReqFileBrowserDeleteImage): Observable<ResFileBrowserGetFile> {
        return this.http.post<ResFileBrowserGetFile>(this.appInitializer.config.apiUrl + ApiUrl.FILE_BROWSER_DELETE_IMAGE, reqFileBrowserDeleteImage);
    }
}