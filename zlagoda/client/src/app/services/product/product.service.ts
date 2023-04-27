import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import {Category} from "../category/category.service";

export type Product = {
  id: number,
  name: string,
  category: Category,
  description: string,
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl = '/products'

  public getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  public getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/$id`);
  }

  public create(product: Product): Observable<Product> {
    console.log(JSON.stringify(product));
    return this.http.post<Product>(this.apiUrl,  JSON.stringify(product));
  }

  public edit(product: Product): Observable<Product> {
    return this.http.put<Product>(this.apiUrl, JSON.stringify(product));
  }

  public delete(product: Product): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${product.id}`);
  }
}
