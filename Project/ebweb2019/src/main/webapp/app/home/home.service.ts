import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class HomeService {
    private resourceUrl = SERVER_API_URL + 'api/inforPackage/warning';

    constructor(private http: HttpClient) {}

    getInfoPackage(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(this.resourceUrl, { params: options, observe: 'response' });
    }

    checkDayBackup(req?: any): Observable<any> {
        const options = createRequestOption(req);
        return this.http.get<any>(SERVER_API_URL + 'api/get-days-backup', { params: options, observe: 'response' });
    }

    getSoDuTaiKhoan() {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/so-du-tai-khoan', { params: {}, observe: 'response' });
    }

    getSucKhoeTaiChinh(req?: any) {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/suc-khoe-tai-chinh-doanh-nghiep', {
            params: req,
            observe: 'response'
        });
    }

    getBieuDoTongHop(req?: any) {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/bieu-do-tong-hop', {
            params: req,
            observe: 'response'
        });
    }

    getBieuDoDoanhThuChiPhi(req?: any) {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/bieu-do-doanh-thu-chi-phi', {
            params: req,
            observe: 'response'
        });
    }

    getBieuDoNoPhaiThuTra(req?: any) {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/bieu-do-no-phai-thu-tra', {
            params: req,
            observe: 'response'
        });
    }

    getAllData(req?: any) {
        return this.http.get<any>(SERVER_API_URL + 'api/trang-chu/load-data', {
            params: req,
            observe: 'response'
        });
    }
}
