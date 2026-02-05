package com.suchit.rdtracker.controller;

import com.suchit.rdtracker.dto.DashboardSummaryDTO;
import com.suchit.rdtracker.dto.PaidCustomerDTO;
import com.suchit.rdtracker.dto.PendingCustomerDTO;
import com.suchit.rdtracker.entity.Customer;
import com.suchit.rdtracker.entity.Payment;
import com.suchit.rdtracker.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/customer/{customerId}")
	public Payment addPayment(@PathVariable Long customerId, @RequestBody Payment payment) {
		return paymentService.addPayment(customerId, payment);
	}

	@GetMapping("/customer/{customerId}")
	public List<Payment> getPayments(@PathVariable Long customerId) {
		return paymentService.getPaymentsByCustomer(customerId);
	}

//    @GetMapping("/pending/current-month")
//    public List<Long> pendingCustomers() {
//        return paymentService.getPendingCustomersForCurrentMonth();
//    }

	@GetMapping("/customer/{customerId}/month")
	public boolean isPaid(@PathVariable Long customerId, @RequestParam YearMonth month) {
		return paymentService.isPaymentDoneForMonth(customerId, month);
	}

	@GetMapping("/pending")
	public List<PendingCustomerDTO> pendingCustomersByMonth(@RequestParam YearMonth month) {
		return paymentService.getPendingCustomersForMonth(month);
	}

	@GetMapping("/dashboard/summary")
	public DashboardSummaryDTO getDashboardSummary(@RequestParam YearMonth month) {
		return paymentService.getDashboardSummary(month);
	}

	@GetMapping("/paid")
	public List<PaidCustomerDTO> getPaidCustomers(@RequestParam YearMonth month) {
		return paymentService.getPaidCustomersForMonth(month);
	}

	@GetMapping("/paid/pdf")
	public ResponseEntity<byte[]> downloadPaidCustomersPdf(@RequestParam YearMonth month) {

		byte[] pdf = paymentService.generatePaidCustomersPdf(month);

		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=RD_Report_" + month + ".pdf")
				.contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(pdf);
	}

	// ✏️ Edit payment
	@PutMapping("/{paymentId}")
	public Payment updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
		return paymentService.updatePayment(paymentId, payment);
	}

	// ❌ Delete payment
	@DeleteMapping("/{paymentId}")
	public void deletePayment(@PathVariable Long paymentId) {
		paymentService.deletePayment(paymentId);
	}

	// 👁️ Get single payment (for edit)
	@GetMapping("/{paymentId}")
	public Payment getPaymentById(@PathVariable Long paymentId) {
		return paymentService.getPaymentById(paymentId);
	}

}
