import {ResponseType} from './enum/ResponseType';

export class ResponseModel {
  message: string;
  responseType: ResponseType;
  result?: [];
}
