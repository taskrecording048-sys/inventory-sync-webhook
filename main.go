package main

import (
	"encoding/json"
	"log"
	"net/http"
)

type InventorySyncEvent struct {
	SKU       string `json:"sku"`
	Quantity  int    `json:"quantity"`
	Warehouse string `json:"warehouse"`
}

func handleInventorySync(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	var event InventorySyncEvent
	if err := json.NewDecoder(r.Body).Decode(&event); err != nil {
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	log.Printf("synced sku=%s quantity=%d warehouse=%s", event.SKU, event.Quantity, event.Warehouse)
	w.WriteHeader(http.StatusOK)
}

func main() {
	http.HandleFunc("/webhook/inventory-sync", handleInventorySync)
	log.Println("inventory-sync-webhook listening on :8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
