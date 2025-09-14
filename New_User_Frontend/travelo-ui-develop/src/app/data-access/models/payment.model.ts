export interface PaymentModel {
    bookingId: number;
    paymentId: string,
    paymentLink?: string,
    status?: string,
    orderId?: string
}