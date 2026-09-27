package main

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestHandleInventorySync(t *testing.T) {
	payload := InventorySyncEvent{SKU: "SKU-1001", Quantity: 42, Warehouse: "west-1"}
	body, _ := json.Marshal(payload)

	req := httptest.NewRequest(http.MethodPost, "/webhook/inventory-sync", bytes.NewReader(body))
	rec := httptest.NewRecorder()

	handleInventorySync(rec, req)

	if rec.Code != http.StatusOK {
		t.Fatalf("expected status 200, got %d", rec.Code)
	}
}

func TestHandleInventorySyncRejectsGet(t *testing.T) {
	req := httptest.NewRequest(http.MethodGet, "/webhook/inventory-sync", nil)
	rec := httptest.NewRecorder()

	handleInventorySync(rec, req)

	if rec.Code != http.StatusMethodNotAllowed {
		t.Fatalf("expected status 405, got %d", rec.Code)
	}
}
