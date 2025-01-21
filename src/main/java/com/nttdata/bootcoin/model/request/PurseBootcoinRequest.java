package com.nttdata.bootcoin.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nttdata.bootcoin.model.enums.TypeDocument;
import com.nttdata.bootcoin.util.DocumentTypeDeserializer;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurseBootcoinRequest {
    @JsonDeserialize(using = DocumentTypeDeserializer.class)
    @NotNull(message = "Document type is required")
    private TypeDocument typeDocument;

    @NotNull(message = "Document number is required")
    @Size(min = 1, max = 8, message = "The ID number must not be greater than 8 or less than 1")
    private int numberDocument;

    @NotNull(message = "Phone number is required")
    @NotEmpty(message = "Phone number is empty")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String numberPhone;

    @NotNull(message = "IMEI phone is required")
    @Min(value = 1, message = "IMEI phone must be greater than 0")
    private int imeiPhone;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 0, message = "The balance must be greater than or equal to 0")
    private double balance;

    @NotEmpty(message = "Number account cannot be empty")
    private String numberAccount;

    public PurseBootcoinRequest(TypeDocument typeDocument, int numberDocument, String numberPhone, int imeiPhone, String email, double balance, String numberAccount) {
        this.typeDocument = typeDocument;
        this.numberDocument = numberDocument;
        this.numberPhone = numberPhone;
        this.imeiPhone = imeiPhone;
        this.email = email;
        this.balance = balance;
        this.numberAccount = numberAccount;
    }
}